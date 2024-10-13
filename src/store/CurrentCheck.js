import { createSlice } from '@reduxjs/toolkit'
import {PassportData, newPassportData, GenderTypes} from "../utils/model";

//Default test data
const defaultCheck= newPassportData('Doe','John','Armenian',new Date(1985, 9, 14).toISOString(),GenderTypes.FEMALE, 'PP','ISR','GTS3850245',new Date(1975, 3, 14).toISOString(),false,true);

//This store contains all the necessary data regarding the current check (Person)
export const currentCheckSlice = createSlice({
    name: 'PassportCheck',
    initialState: {
        value:  defaultCheck
    },
    reducers: {
        //Sets the data captured from the MRZ
        setMRZData: (state, action) => {
            state.value.user = action.payload
            //state.value.push(action.payload)
        },
    },
})

// Action creators are generated for each case reducer function
export const { setMRZData } = currentCheckSlice.actions

export default currentCheckSlice.reducer
