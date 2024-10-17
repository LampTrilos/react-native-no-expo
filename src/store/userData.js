import { createSlice } from '@reduxjs/toolkit'
import {axiosGet, axiosPost, axiosPut, axiosDelete} from "../utils/axiosCalls";
import {newUser, UserClass} from "../utils/model";

export const userDataSlice = createSlice({
    name: 'userData',
    initialState: {
        value: {
            user: newUser('Arxikos user', '234532', '08:00 - 19:00')
        },
    },
    reducers: {
        //Sets the current User of the application, after the axios call will have returned the currentUser
        setUser: (state, action) => {
            state.value.user = action.payload
            //state.value.push(action.payload)
        },
    },
})

// Action creators are generated for each case reducer function
export const { setUser } = userDataSlice.actions

export default userDataSlice.reducer
