import React from "react";
import {StyleSheet, View} from "react-native";
import CustomTheme from "../../../assets/Theme"
import PersonInformationNFC from "./PersonInformationNFC.tsx";
import SearchResults from "./SearchResults.tsx";
import PassportScan from "./PassportScan.tsx";

export default function PassportCheck() {
    const personData = {
        surname: 'Doe',
        name: 'John',
        dateOfBirth: new Date(1985, 9, 14) // October 14, 1985
    };

    return (
        <View style={styles.containerColumn}>
            <View style={styles.containerRow}>
                <PassportScan style={styles.subscreen}/>
                <PersonInformationNFC person={personData} style={styles.subscreen}/>
            </View>
            <View style={styles.containerRow}>
                <SearchResults style={styles.subscreen}/>
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
        margin: 5,
    }
})
