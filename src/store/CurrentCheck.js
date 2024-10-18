import { createSlice } from '@reduxjs/toolkit'
import {PassportData, newPassportData, GenderTypes, BorderCheck} from "../utils/model";

//Default test data
const defaultNFCData= newPassportData('https://thumbs.mugshots.com/gallery/images/f0/ef/JONATHAN-XAVIER-CABRAL-mugshot-50338840.400x800.jpg', 'Doe','John','Armenian',new Date(1985, 9, 14).toISOString(),GenderTypes.FEMALE, 'PP','ISR','GTS3850245',new Date(1975, 3, 14).toISOString(),false,false);

//This store contains all the necessary data regarding the current check (Person)
export const currentCheckSlice = createSlice({
    name: 'PassportCheck',
    initialState: {
        //Just the first step of scanning mrz data, we keep it to compare it to the NFC Scan later
        mrzData: {},
        mrzChecked: false,
        //The data scanned via nfc from passport
        nfcData:  defaultNFCData,
        chipChecked: false,
        //The face image of the checked person, in  Base64-encoded string
        faceImage: "",
        //The fingerprint file of the checked person, in  Base64-encoded string
        fingerprints: "",
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
            state.mrzChecked =  true
        },
        //Sets the data captured from the MRZ
        setNFCData: (state, action) => {
            //Change this code to reflect the actual NFC Data
            state.nfcData.faceImage =  action.payload.faceImage
            state.nfcData.documentNumber =  action.payload.documentNumber
            state.nfcData.dateOfBirth =  action.payload.dateOfBirth
            state.nfcData.dateOfExpiry =  action.payload.dateOfExpiry
            state.nfcData.familyName =  action.payload.familyName
            state.nfcData.firstName =  action.payload.firstName
            state.nfcData.nationality =  action.payload.nationality
            state.nfcData.gender =  action.payload.gender
            state.nfcData.issueCountry =  action.payload.issueCountry
            state.nfcData.type =  action.payload.type
            //state.value.mrzChecked =  true
            state.chipChecked =  true
        },
    },
})

// Action creators are generated for each case reducer function
export const { setMRZData, setNFCData } = currentCheckSlice.actions

export default currentCheckSlice.reducer
