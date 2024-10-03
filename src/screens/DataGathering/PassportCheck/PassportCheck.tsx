import React from "react";
import {StyleSheet, View} from "react-native";
import CustomTheme from "../../../assets/Theme"
import PersonInformationNFC from "./PersonInformationNFC.tsx";
import PassportScan from "./PassportScan.tsx";
import {GenderTypes, newNFCData} from "../../../utils/model.ts";

export default function PassportCheck() {


    const nfcData = newNFCData('Doe','John','Armenian',new Date(1985, 9, 14),GenderTypes.FEMALE, 'PP','ISR','GTS3850245',new Date(1975, 3, 14),false,true);

    return (
        <View style={styles.containerColumn}>
            <View style={styles.containerRow}>
                <PassportScan style={styles.subscreen}/>
                <PersonInformationNFC person={nfcData} style={styles.subscreen}/>
            </View>
            {/*<View style={styles.containerRow}>*/}
            {/*    <SearchResults style={styles.subscreen}/>*/}
            {/*</View>*/}
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
