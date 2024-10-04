import { createSlice } from '@reduxjs/toolkit'
import {axiosGet, axiosPost, axiosPut, axiosDelete} from "../utils/axiosCall";
import {ControlStatusTypes} from "../utils/model";

export const controlStatusSlice = createSlice({
    name: 'controlStatus',
    initialState: {
        value: {
            currentStatus: ControlStatusTypes.None
        },
    },
    reducers: {
        //Sets the current User of the application, after the axios call will have returned the currentUser
        setStatus: (state, action) => {
            state.value.currentStatus = action.payload
            //state.value.push(action.payload)
        },
    },
})

// Action creators are generated for each case reducer function
export const { setStatus } = controlStatusSlice.actions

export default controlStatusSlice.reducer
