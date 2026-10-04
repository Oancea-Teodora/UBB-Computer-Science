import logging
from typing import Any, Dict, List, Optional

import aiosqlite

log = logging.getLogger("recipes-db")

DB_PATH = "recipes.db"


async def init_db():
    """Initialize the database with the recipes table"""
    async with aiosqlite.connect(DB_PATH) as db:
        await db.execute("""
            CREATE TABLE IF NOT EXISTS recipes (
                id TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                ingredients TEXT NOT NULL,
                steps TEXT NOT NULL,
                preparationTime INTEGER NOT NULL,
                dateCreated INTEGER NOT NULL
            )
        """)
        await db.commit()
    log.info(f"Database initialized at {DB_PATH}")


async def fetch_all() -> List[Dict[str, Any]]:
    """Fetch all recipes from the database"""
    async with aiosqlite.connect(DB_PATH) as db:
        db.row_factory = aiosqlite.Row
        cur = await db.execute("SELECT * FROM recipes ORDER BY id DESC")
        rows = await cur.fetchall()
        result = [dict(r) for r in rows]
    log.debug(f"Fetched all recipes: {len(result)} found")
    return result


async def fetch_one(recipe_id: str) -> Optional[Dict[str, Any]]:
    """Fetch a single recipe by ID"""
    async with aiosqlite.connect(DB_PATH) as db:
        db.row_factory = aiosqlite.Row
        cur = await db.execute("SELECT * FROM recipes WHERE id = ?", (recipe_id,))
        row = await cur.fetchone()
        result = dict(row) if row else None
    log.debug(f"Fetched recipe {recipe_id}: {'found' if result else 'not found'}")
    return result


async def insert_recipe(data) -> Dict[str, Any]:
    """Insert a new recipe into the database"""
    async with aiosqlite.connect(DB_PATH) as db:
        cur = await db.execute(
            """
            INSERT OR REPLACE INTO recipes (id, title, ingredients, steps, preparationTime, dateCreated)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
            (data.id, data.title, data.ingredients, data.steps, data.preparationTime, data.dateCreated),
        )
        await db.commit()
    created = await fetch_one(data.id)
    log.info(f"Inserted recipe ID {data.id}: {created['title']}")
    return created


async def update_recipe(recipe_id: str, data) -> bool:
    """Update an existing recipe"""
    async with aiosqlite.connect(DB_PATH) as db:
        cur = await db.execute(
            """
            UPDATE recipes
            SET title=?, ingredients=?, steps=?, preparationTime=?, dateCreated=?
            WHERE id=?
            """,
            (data.title, data.ingredients, data.steps,
             data.preparationTime, data.dateCreated, recipe_id),
        )
        await db.commit()
        ok = cur.rowcount > 0
    if ok:
        log.info(f"Updated recipe ID {recipe_id}")
    else:
        log.warning(f"Update recipe ID {recipe_id}: not found")
    return ok


async def delete_recipe(recipe_id: str) -> bool:
    """Delete a recipe from the database"""
    async with aiosqlite.connect(DB_PATH) as db:
        cur = await db.execute("DELETE FROM recipes WHERE id = ?", (recipe_id,))
        await db.commit()
        ok = cur.rowcount > 0
    if ok:
        log.info(f"Deleted recipe ID {recipe_id}")
    else:
        log.warning(f"Delete recipe ID {recipe_id}: not found")
    return ok
