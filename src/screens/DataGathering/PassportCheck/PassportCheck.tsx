import React, {useEffect, useState} from "react";
import {StyleSheet, View} from "react-native";
import CustomTheme from "../../../assets/Theme"
import PersonInformationNFC from "./PersonInformationNFC.tsx";
import PersonInformationMRZ from "./PersonInformationMRZ.tsx";
//import {GenderTypes, newPassportData} from "../../../utils/model.ts";
import {useDispatch, useSelector} from "react-redux";
import {Button} from "react-native-paper";

//This page appears after the StartCheck page, after the user has selected to start the document check procedure
export default function PassportCheck() {
    useEffect(() => {
        console.log('PassportCheck is beginning..')
    }, []);

    //To check if th user has selected to start the check
    const [isCheckStarted, setIsCheckStarted] = useState(false);

    const onPress = () => {
        setIsCheckStarted(true)
    };
    //const nfcData = newNFCData('Doe','John','Armenian',new Date(1985, 9, 14),GenderTypes.FEMALE, 'PP','ISR','GTS3850245',new Date(1975, 3, 14),false,true);
    //-------------Store section about the currentCheck(Person)---------------------//
     const currentCheck = useSelector(state => state.currentCheckStore);
    // const dispatch = useDispatch();
    //-------------End of store section about the currentCheck(Person)---------------------//


//Originally i tried to separate this logic with 2 screens and a StackNavigator, but the componenets kept rerendering ad infinitum
    return (
        <>
            {!isCheckStarted ?
                ( <View style={styles.container}>
            <Button mode="contained" onPress={onPress} style={styles.button}>
                Εκκίνιση Ελέγχου
            </Button>
        </View>
                ) :
                ( <View style={styles.containerColumn}>
            <View style={styles.containerRow}>
                <PersonInformationMRZ style={styles.subscreen1}/>
                <PersonInformationNFC currentCheck={currentCheck} style={styles.subscreen2}/>
            </View>
        </View> )}
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
    containerColumn: {
        flex: 1,
        flexDirection: 'column', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
        marginTop: 5,
    },
    subscreen1: {
        flex: 2,
        margin: 6,
        //marginBottom: 80
    },
    subscreen2: {
        flex: 3,
        margin: 6,
        //marginBottom: 80
    },
    container: {
        flex: 1,
        width: '100%',
        alignItems: 'center',
        backgroundColor: 'white',
        padding: 15,
        borderRadius: 15,
    },
    button: {
        top: '45%'
    }
})
