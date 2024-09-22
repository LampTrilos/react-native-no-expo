import { createSlice } from '@reduxjs/toolkit'
import {axiosGet, axiosPost, axiosPut, axiosDelete} from "../utils/axiosCall";

export const groceryHistSlice = createSlice({
    name: 'groceryHist',
    initialState: {
        value: [],
    },
    reducers: {
        // addItem: (state, action) => {
        //     // Redux Toolkit allows us to write "mutating" logic in reducers. It
        //     // doesn't actually mutate the state because it uses the Immer library,
        //     // which detects changes to a "draft state" and produces a brand new
        //     // immutable state based off those changes.
        //     // Also, no return statement is required from these functions.
        //     // @ts-ignore
        //     //console.log(action.payload)
        //     // Check if the item with the same id already exists in the list
        //     const itemExists = state.value.some(item => item.title === action.payload.title);
        //     // If the item does not exist, add it to the list
        //     if (!itemExists) {
        //         state.value.push(action.payload)
        //         state.value = reOrderList(state.value)
        //         axiosPost('groceryList', action.payload)
        //     }
        // },
        // editItem: (state, action) => {
        //     //console.log('store')
        //     //console.log(state.value)
        //     //console.log(action.payload)
        //     //Replace an element of a js array with another based on the id
        //     state.value =  state.value.map(item => action.payload.id === item.id ? action.payload : item);
        //     //console.log(state.value)
        //     axiosPut('groceryList', action.payload)
        //     state.value = reOrderList(state.value)
        // },
        removeItem: (state, action) => {
            //console.log('Payload is ' + action.payload.id)
            // @ts-ignore
            state.value = state.value.filter(item => item.id !== action.payload.id)
            //console.log(state.value)
            axiosDelete('groceryHist', action.payload)
            state.value = reOrderList(state.value)
        },
        setItems:(state, action) => {
            state.value = [action.payload]
            state.value = reOrderList(state.value)
            //console.log(state.value)
        }
        // incrementByAmount: (state, action) => {
        //     state.value += action.payload
        // },
    },
})

//This function  rearranges the list  every time a change occurs, constant items come on top
function reOrderList(itemList) {
    let tempList = []
    //Because firebase returns an array of arrays
    if (Array.isArray(itemList[0])) {
        tempList =  [...itemList[0]]
    }
    else {
        tempList =  [...itemList]
    }

    tempList = tempList.sort((a, b) => {
        // First sort by 'discount' to put discount items last
        if (a.discount !== b.discount) {
            return a.discount ? 1 : -1; // Discounts go last
        }
        // Finally sort alphabetically by 'title' in ascending order
        return a.title.localeCompare(b.title); // Alphabetical order
    });
    return tempList;
}



// Action creators are generated for each case reducer function
export const { addItem, editItem,  removeItem, setItems } = groceryHistSlice.actions

export default groceryHistSlice.reducer
