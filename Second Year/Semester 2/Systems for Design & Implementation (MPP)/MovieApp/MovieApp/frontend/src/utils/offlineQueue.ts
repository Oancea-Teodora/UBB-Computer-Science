export interface OfflineOperation {
    type: "add" | "update" | "delete";
    payload: any;
}

export const getOfflineQueue = (): OfflineOperation[] => {
    const queueStr = localStorage.getItem("offlineQueue");
    if (queueStr) {
        try {
            return JSON.parse(queueStr) as OfflineOperation[];
        } catch (e) {
            console.error("Error parsing offlineQueue", e);
            return [];
        }
    }
    return [];
};

export const saveOfflineQueue = (queue: OfflineOperation[]) => {
    localStorage.setItem("offlineQueue", JSON.stringify(queue));
};

export const queueOperation = (operation: OfflineOperation) => {
    const queue = getOfflineQueue();
    queue.push(operation);
    saveOfflineQueue(queue);
};

export const processQueue = async () => {
    const queue = getOfflineQueue();
    for (const op of queue) {
        try {
            if (op.type === "add") {
                await fetch("/movies", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(op.payload),
                });
            } else if (op.type === "update") {
                await fetch(`/movies/${op.payload.id}`, {
                    method: "PATCH",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(op.payload),
                });
            } else if (op.type === "delete") {
                await fetch(`/movies/${op.payload.id}`, { method: "DELETE" });
            }
        } catch (error) {
            console.error("Failed to process offline operation:", op, error);
            // If one fails, exit processing so we can try again later.
            return;
        }
    }
    // Clear the queue if all operations succeeded.
    localStorage.removeItem("offlineQueue");
};
