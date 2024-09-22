import { configureStore } from '@reduxjs/toolkit'
import groceryListReducer from "./groceryList";
import groceryHistReducer from "./groceryHist";
import recipeListReducer from "./recipeList";

export default configureStore({
     reducer: {
          groceryList: groceryListReducer,
          groceryHist: groceryHistReducer,
          recipeList: recipeListReducer,
     },
})
