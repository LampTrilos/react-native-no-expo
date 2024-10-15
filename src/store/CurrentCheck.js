import { createSlice } from '@reduxjs/toolkit'
import {PassportData, newPassportData, GenderTypes} from "../utils/model";

//Default test data
const defaultCheck= newPassportData('https://thumbs.mugshots.com/gallery/images/f0/ef/JONATHAN-XAVIER-CABRAL-mugshot-50338840.400x800.jpg', 'Doe','John','Armenian',new Date(1985, 9, 14).toISOString(),GenderTypes.FEMALE, 'PP','ISR','GTS3850245',new Date(1975, 3, 14).toISOString(),false,false);

//This store contains all the necessary data regarding the current check (Person)
export const currentCheckSlice = createSlice({
    name: 'PassportCheck',
    initialState: {
        //Just the first step of scanning mrz data, we keep it to compare it to the NFC Scan later
        mrzData: {},
        //The overall check of a person
        value:  defaultCheck,
    },
    reducers: {
        //Sets the data captured from the MRZ
        setMRZData: (state, action) => {
            //First we save the MRZ Dara object that will be shown in Passport Scan
            state.mrzData.documentNumber =  action.payload.documentNumber
            state.mrzData.dateOfBirth =  action.payload.dateOfBirth
            state.mrzData.dateOfExpiry =  action.payload.dateOfExpiry
            //Then we save the values for the overall Check
            // state.value.documentNumber =  action.payload.documentNumber
            // state.value.dateOfBirth =  action.payload.dateOfBirth
            // state.value.dateOfExpiry =  action.payload.dateOfExpiry
            state.value.mrzChecked =  true
        },
        //Sets the data captured from the MRZ
        setNFCData: (state, action) => {
            //Change this code to reflect the actual NFC Data
            state.value.faceImage =  action.payload.faceImage
            state.value.documentNumber =  action.payload.documentNumber
            state.value.dateOfBirth =  action.payload.dateOfBirth
            state.value.dateOfExpiry =  action.payload.dateOfExpiry
            state.value.familyName =  action.payload.familyName
            state.value.firstName =  action.payload.firstName
            state.value.nationality =  action.payload.nationality
            state.value.gender =  action.payload.gender
            state.value.issueCountry =  action.payload.issueCountry
            state.value.type =  action.payload.type
            //state.value.mrzChecked =  true
            state.value.chipChecked =  true
        },
    },
})

// Action creators are generated for each case reducer function
export const { setMRZData, setNFCData } = currentCheckSlice.actions

export default currentCheckSlice.reducer
