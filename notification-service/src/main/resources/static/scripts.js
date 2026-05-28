const WS_URL = `ws:
        const RECONNECT_BASE_MS = 1000;
        const RECONNECT_MAX_MS = 30000;

        let ws = null;
        let reconnectAttempt = 0;
        let reconnectTimer = null;
        let notificationCount = 0;

        const ICONS = {
            "app-plus": "📗",
            "app-edit": "📝",
            "app-remove": "🗑️",
            analytics: "🔬",
            "user-plus": "👤",
            "user-remove": "❌",
            bell: "🔔",
        };

        const $status = document.getElementById("status");
        const $statusText = document.getElementById("status-text");
        const $badge = document.getElementById("badge");
        const $list = document.getElementById("notifications");
        const $empty = document.getElementById("empty-state");
        const $log = document.getElementById("log-area");

        function connect() {
            if (
                ws &&
                (ws.readyState === WebSocket.OPEN ||
                    ws.readyState === WebSocket.CONNECTING)
            ) {
                return;
            }

            setStatus("reconnecting", "Подключение...");
            addLog("info", `Подключение к ${WS_URL}...`);

            ws = new WebSocket(WS_URL);

            ws.onopen = () => {
                reconnectAttempt = 0;
                setStatus("connected", "Подключено");
                addLog("info", "WebSocket подключён");
            };

            ws.onmessage = (event) => {
                try {
                    const data = JSON.parse(event.data);

                    if (data.type === "CONNECTED") {
                        addLog(
                            "info",
                            `Сервер: ${data.message} (подключений: ${data.activeConnections})`,
                        );
                        return;
                    }

                    if (data.type === "PONG") {
                        addLog("info", "Pong получен");
                        return;
                    }

                    if (data.type === "NOTIFICATION") {
                        addNotification(data);
                    }
                } catch (e) {
                    addLog("error", `Ошибка парсинга: ${e.message}`);
                }
            };

            ws.onclose = (event) => {
                setStatus("disconnected", "Отключено");
                addLog(
                    "warn",
                    `WebSocket закрыт (code: ${event.code}, reason: ${event.reason || "нет"})`,
                );
                scheduleReconnect();
            };

            ws.onerror = () => {
                addLog("error", "Ошибка WebSocket");
            };
        }

        function scheduleReconnect() {
            const delay = Math.min(
                RECONNECT_BASE_MS * Math.pow(2, reconnectAttempt),
                RECONNECT_MAX_MS,
            );
            reconnectAttempt++;
            setStatus(
                "reconnecting",
                `Переподключение через ${(delay / 1000).toFixed(0)}с...`,
            );
            addLog(
                "warn",
                `Переподключение #${reconnectAttempt} через ${delay}мс`,
            );
            clearTimeout(reconnectTimer);
            reconnectTimer = setTimeout(connect, delay);
        }

        function setStatus(state, text) {
            $status.className = `status ${state}`;
            $statusText.textContent = text;
        }

        function addNotification(data) {
            $empty.style.display = "none";
            notificationCount++;
            $badge.textContent = notificationCount;

            const card = document.createElement("div");
            card.className = `notification level-${data.level || "info"} is-new`;

            const iconClass = `icon-${data.icon || "bell"}`;
            const iconEmoji = ICONS[data.icon] || "🔔";

            const eventTime = data.eventTimestamp
                ? new Date(data.eventTimestamp).toLocaleTimeString("ru-RU")
                : "";
            const receivedTime = data.receivedAt
                ? new Date(data.receivedAt).toLocaleTimeString("ru-RU")
                : new Date().toLocaleTimeString("ru-RU");

            card.innerHTML = `
        <div class="notification-icon ${iconClass}">${iconEmoji}</div>
        <div class="notification-body">
            <div class="notification-title">${escapeHtml(data.title)}</div>
            <div class="notification-desc">${escapeHtml(data.description)}</div>
            <div class="notification-meta">
                <span>${receivedTime}</span>
                <span>${escapeHtml(data.source)}</span>
                <span>${escapeHtml(data.eventType)}</span>
            </div>
        </div>
    `;

            $list.prepend(card);

            setTimeout(() => card.classList.remove("is-new"), 1500);

            document.title = `(${notificationCount}) Центр уведомлений`;
        }

        function clearAll() {
            $list.innerHTML = "";
            notificationCount = 0;
            $badge.textContent = "0";
            $empty.style.display = "";
            document.title = "Центр уведомлений";
        }

        function addLog(level, message) {
            const entry = document.createElement("div");
            entry.className = `log-entry ${level}`;
            const time = new Date().toLocaleTimeString("ru-RU");
            entry.textContent = `[${time}] ${message}`;
            $log.appendChild(entry);
            $log.scrollTop = $log.scrollHeight;
        }

        function toggleLog() {
            $log.classList.toggle("visible");
        }

        function escapeHtml(str) {
            if (!str) return "";
            const div = document.createElement("div");
            div.textContent = str;
            return div.innerHTML;
        }

        connect();