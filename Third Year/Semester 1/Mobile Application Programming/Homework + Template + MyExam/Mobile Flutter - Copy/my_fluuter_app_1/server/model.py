from pydantic import BaseModel


class RecipeIn(BaseModel):
    id: str
    title: str
    ingredients: str
    steps: str
    preparationTime: int
    dateCreated: int


class RecipeUpdate(BaseModel):
    id: str
    title: str
    ingredients: str
    steps: str
    preparationTime: int
    dateCreated: int


class RecipeOut(RecipeUpdate):
    pass

