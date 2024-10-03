import React from "react";
import {StyleSheet, View} from "react-native";
import CustomTheme from "../../../../assets/Theme"
import EESResultsTable from "./EESResultsTable";
import PoliceChecksTable from "./PoliceChecksTable";

export default function SearchResults() {


    return (<>
            <View style={styles.containerRow}>
                <EESResultsTable style={styles.subscreen}/>
                <PoliceChecksTable style={styles.subscreen}/>
            </View>
        </>
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
    subscreen: {
        flex: 1,
        margin: 5,
    }
})
