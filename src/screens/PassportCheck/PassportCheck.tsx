
import React from "react";
import {StyleSheet, View} from "react-native";
import CustomTheme from "../../assets/Theme"
import PersonInformation from "./PersonInformation.tsx";
import SearchResults from "./SearchResults.tsx";

export default function PassportCheck() {
    const personData = {
        surname: 'Doe',
        name: 'John',
        dateOfBirth: new Date(1985, 9, 14) // October 14, 1985
    };

    return (
        <View style={styles.container}>
            <SearchResults style={styles.subscreen}/>
            <PersonInformation person={personData} style={styles.subscreen}/>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
    },
    subscreen: {
        flex: 1,
        margin: 5,
    }
})
