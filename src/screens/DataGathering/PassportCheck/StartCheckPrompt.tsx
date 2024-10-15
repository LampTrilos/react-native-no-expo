import React from "react";
import {Button, Card, Text} from "react-native-paper";
import {NativeModules, StyleSheet, View} from 'react-native';
import {Navigation} from "../../../utils/types.tsx";
import Header from '../../../components/Header';
import customTheme from "../../../assets/Theme";

type Props = {
    navigation: Navigation;
};

//This page has only a button to commence the procedure of passport check
export default function StartCheckPrompt({ navigation }: Props) {
    const onPress = () => {
        //If all is well, navigate to the PassportCheck Screen, that contains the tabs for the logic of Passport Control
        navigation.navigate('PassportCheck');
    };
    return (

            <View style={styles.container}>
                {/*<Header >Entry Exit System</Header>*/}
                <Button mode="contained" onPress={onPress} style={styles.button}>
                    Εκκίνιση Ελέγχου
                </Button>
            </View>
    );
};

const styles = StyleSheet.create({
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
});
