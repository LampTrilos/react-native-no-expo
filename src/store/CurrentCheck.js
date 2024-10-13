import { createSlice } from '@reduxjs/toolkit'
import {PassportData, newPassportData, GenderTypes} from "../utils/model";

//Default test data
const defaultCheck= newPassportData('Doe','John','Armenian',new Date(1985, 9, 14).toISOString(),GenderTypes.FEMALE, 'PP','ISR','GTS3850245',new Date(1975, 3, 14).toISOString(),false,false);

//This store contains all the necessary data regarding the current check (Person)
export const currentCheckSlice = createSlice({
    name: 'PassportCheck',
    initialState: {
        value:  defaultCheck
    },
    reducers: {
        //Sets the data captured from the MRZ
        setMRZData: (state, action) => {
            state.value.documentNumber =  action.payload.documentNumber
            state.value.dateOfBirth =  action.payload.dateOfBirth
            state.value.dateOfExpiry =  action.payload.dateOfExpiry
            state.value.mrzChecked =  true
        },
    },
})

// Action creators are generated for each case reducer function
export const { setMRZData } = currentCheckSlice.actions

export default currentCheckSlice.reducer
