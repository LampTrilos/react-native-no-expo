import { configureStore } from '@reduxjs/toolkit'
import userDataReducer from "./userData";
import controlStatusReducer from "./controlStatus";
import currentCheckReducer from "./CurrentCheck";
// import groceryListReducer from "./groceryList";
// import groceryHistReducer from "./groceryHist";
// import recipeListReducer from "./recipeList";

export default configureStore({
     //This unifies all the store slices into a universal stores
     reducer: {
          //The logged in user
          userDataStore: userDataReducer,
          //The current status of the border check
          controlStatusStore: controlStatusReducer,
          //The current check that is ongoing (Person that is being checked)
          currentCheckStore: currentCheckReducer,
     },
})
