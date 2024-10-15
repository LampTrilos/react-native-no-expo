import React, {useEffect} from "react";
import StartCheckPrompt from "../PassportCheck/StartCheckPrompt.tsx";
import PassportCheck from "../PassportCheck/PassportCheck.tsx";
import {createStackNavigator} from "@react-navigation/stack";

//This page contains the navigation for the StartCheck page that leads to the PassportCheck page, which contains all the tabs for the Passport Control Check
export default function PassportNavigator() {
    const Stack = createStackNavigator();

    return (
        <>
        <Stack.Navigator initialRouteName="Login" >
            <Stack.Screen name="StartCheckPrompt" component={StartCheckPrompt}  options={{ headerShown: false }} />
            <Stack.Screen name="PassportCheck" component={PassportCheck} options={{ headerShown: false }}/>
        </Stack.Navigator>
        </>
        // <View style={styles.containerColumn}>
        //
        //     <View style={styles.containerRow}>
        //         <PersonInformationMRZ style={styles.subscreen}/>
        //         <PersonInformationNFC travelDocument={currentCheck} style={styles.subscreen}/>
        //     </View>
        // </View>
    )
        ;
};
