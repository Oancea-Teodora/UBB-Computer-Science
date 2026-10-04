import json
import logging
from contextlib import asynccontextmanager
from typing import Any, Dict, Set

from fastapi import FastAPI, WebSocket, WebSocketDisconnect, HTTPException, Body
from fastapi.middleware.cors import CORSMiddleware

import db
from model import RecipeIn, RecipeUpdate


logging.basicConfig(
    level=logging.DEBUG,
    format="%(asctime)s [%(levelname)s] %(name)s - %(message)s",
)
log = logging.getLogger("recipes-server")


@asynccontextmanager
async def lifespan(app: FastAPI):
    """Lifespan context manager for startup and shutdown events"""
    await db.init_db()
    log.info("Server started")
    yield
    log.info("Server shutting down")


app = FastAPI(title="Recipes Server", lifespan=lifespan)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

ws_clients: Set[WebSocket] = set()


def friendly_error(msg: str = "Something went wrong. Please try again."):
    """Return a friendly error message"""
    log.debug(f"Friendly error: {msg}")
    return {"error": msg}


async def broadcast(event_type: str, payload: Any):
    """Broadcast a message to all connected WebSocket clients"""
    if not ws_clients:
        log.debug(f"No clients to broadcast {event_type} to")
        return
    message = json.dumps({"type": event_type, "data": payload})
    dead = []
    for ws in ws_clients:
        try:
            log.debug(f"Broadcasting {event_type} to client")
            await ws.send_text(message)
        except Exception as e:
            log.warning(f"Failed to send to client: {e}")
            dead.append(ws)
    for ws in dead:
        ws_clients.discard(ws)


@app.websocket("/ws")
async def ws_endpoint(ws: WebSocket):
    """WebSocket endpoint for real-time recipe updates"""
    await ws.accept()
    ws_clients.add(ws)
    log.info(f"WS client connected. Total clients: {len(ws_clients)}")
    try:
        snapshot = await db.fetch_all()
        log.debug(f"Sending snapshot with {len(snapshot)} recipes")
        await ws.send_text(json.dumps({"type": "snapshot", "data": snapshot}))
        
        while True:
            await ws.receive_text()
    except WebSocketDisconnect:
        log.info("WS client disconnected")
    except Exception as e:
        log.error(f"WebSocket error: {e}")
    finally:
        ws_clients.discard(ws)
        log.info(f"WS client removed. Total clients: {len(ws_clients)}")



@app.get("/recipes")
async def get_recipes():
    """Get all recipes from the server"""
    log.debug("GET /recipes")
    try:
        recipes = await db.fetch_all()
        log.info(f"GET /recipes returned {len(recipes)} recipes")
        return recipes
    except Exception as e:
        log.exception(f"GET /recipes failed: {e}")
        raise HTTPException(status_code=500, detail=friendly_error("Could not fetch recipes."))


@app.post("/recipes")
async def create_recipe(recipe: RecipeIn):
    """Create a new recipe"""
    log.debug(f"POST /recipes - title: {recipe.title}")
    try:
        created = await db.insert_recipe(recipe)
        log.info(f"Created recipe: ID {created['id']}, title: {created['title']}")
        await broadcast("created", created)
        return created
    except Exception as e:
        log.exception(f"POST /recipes failed: {e}")
        raise HTTPException(status_code=500, detail=friendly_error("Could not create recipe."))


@app.put("/recipes/{recipe_id}")
async def update_recipe_put(recipe_id: str, recipe: RecipeIn):
    """Update a recipe (PUT method)"""
    log.debug(f"PUT /recipes/{recipe_id}")
    try:
        ok = await db.update_recipe(recipe_id, recipe)
        if not ok:
            log.warning(f"PUT /recipes/{recipe_id}: recipe not found")
            raise HTTPException(status_code=404, detail=friendly_error("Recipe not found."))
        updated = await db.fetch_one(recipe_id)
        log.info(f"Updated recipe ID {recipe_id}: {updated['title']}")
        await broadcast("updated", updated)
        return updated
    except HTTPException:
        raise
    except Exception as e:
        log.exception(f"PUT /recipes/{recipe_id} failed: {e}")
        raise HTTPException(status_code=500, detail=friendly_error("Could not update recipe."))


@app.post("/recipes/update")
async def update_recipe_post(recipe: RecipeUpdate):
    """Update a recipe (POST method)"""
    log.debug(f"POST /recipes/update id={recipe.id}")
    try:
        ok = await db.update_recipe(recipe.id, recipe)
        if not ok:
            log.warning(f"POST /recipes/update: recipe {recipe.id} not found")
            raise HTTPException(status_code=404, detail=friendly_error("Recipe not found."))
        updated = await db.fetch_one(recipe.id)
        log.info(f"Updated recipe ID {recipe.id}: {updated['title']}")
        await broadcast("updated", updated)
        return updated
    except HTTPException:
        raise
    except Exception as e:
        log.exception(f"POST /recipes/update failed: {e}")
        raise HTTPException(status_code=500, detail=friendly_error("Could not update recipe."))


@app.delete("/recipes/{recipe_id}")
async def delete_recipe_delete(recipe_id: str):
    """Delete a recipe (DELETE method)"""
    log.debug(f"DELETE /recipes/{recipe_id}")
    try:
        ok = await db.delete_recipe(recipe_id)
        if not ok:
            log.warning(f"DELETE /recipes/{recipe_id}: recipe not found")
            raise HTTPException(status_code=404, detail=friendly_error("Recipe not found."))
        log.info(f"Deleted recipe ID {recipe_id}")
        await broadcast("deleted", {"id": recipe_id})
        return {"ok": True, "id": recipe_id}
    except HTTPException:
        raise
    except Exception as e:
        log.exception(f"DELETE /recipes/{recipe_id} failed: {e}")
        raise HTTPException(status_code=500, detail=friendly_error("Could not delete recipe."))


@app.post("/recipes/delete")
async def delete_recipe_post(payload: Dict[str, Any] = Body(...)):
    """Delete a recipe (POST method)"""
    log.debug(f"POST /recipes/delete payload={payload}")
    recipe_id = payload.get("id")
    if recipe_id is None:
        log.warning("POST /recipes/delete: missing id")
        raise HTTPException(status_code=400, detail=friendly_error("Missing id."))
    try:
        recipe_id = int(recipe_id)
        ok = await db.delete_recipe(recipe_id)
        if not ok:
            log.warning(f"POST /recipes/delete: recipe {recipe_id} not found")
            raise HTTPException(status_code=404, detail=friendly_error("Recipe not found."))
        log.info(f"Deleted recipe ID {recipe_id}")
        await broadcast("deleted", {"id": recipe_id})
        return {"ok": True, "id": recipe_id}
    except HTTPException:
        raise
    except Exception as e:
        log.exception(f"POST /recipes/delete failed: {e}")
        raise HTTPException(status_code=500, detail=friendly_error("Could not delete recipe."))


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000, log_level="info")
