import React, {useEffect} from "react";
import {StyleSheet, View} from "react-native";
import CustomTheme from "../../../assets/Theme"
import PersonInformationNFC from "./PersonInformationNFC.tsx";
import PassportScan from "./PassportScan.tsx";
//import {GenderTypes, newPassportData} from "../../../utils/model.ts";
import {useDispatch, useSelector} from "react-redux";

//This page appears after the StartCheck page, after the user has selected to start the document check procedure
export default function PassportCheck() {


    //const nfcData = newNFCData('Doe','John','Armenian',new Date(1985, 9, 14),GenderTypes.FEMALE, 'PP','ISR','GTS3850245',new Date(1975, 3, 14),false,true);
    //-------------Store section about the currentCheck(Person)---------------------//
    const currentCheck = useSelector(state => state.currentCheckStore.value);
    const dispatch = useDispatch();
    //-------------End of store section about the currentCheck(Person)---------------------//



    return (
        <View style={styles.containerColumn}>
            <View style={styles.containerRow}>
                <PassportScan style={styles.subscreen}/>
                <PersonInformationNFC travelDocument={currentCheck} style={styles.subscreen}/>
            </View>
        </View>
    )
        ;
};

const styles = StyleSheet.create({
    containerRow: {
        flex: 1,
        flexDirection: 'row', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
        marginBottom: 5,
    },
    containerColumn: {
        flex: 1,
        flexDirection: 'column', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
        marginTop: 5,
    },
    subscreen: {
        flex: 1,
        margin: 6,
        //marginBottom: 80
    }
})
