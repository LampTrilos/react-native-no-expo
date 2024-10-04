import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {IconButton} from "react-native-paper";

//This component shows a text and either a green check or a red X, depending on the truthiness of a value pass
//Also shows a small text . All atributes are passed as props
export default function CheckIcon({attributeChecked, textShown}) {
    return (
        <View style={{flexDirection: 'row', alignItems: 'center', marginBottom: -25}}>
            {attributeChecked ? (
                <IconButton icon="check" iconColor="green" size={24}/>
            ) : (
                <IconButton icon="close" iconColor="red" size={24}/>
            )}
            <Text style={{fontSize: 14}}>{textShown}</Text>
        </View>);
}
