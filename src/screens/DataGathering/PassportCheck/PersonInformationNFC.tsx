import React from 'react';
import {View, ScrollView, Image, StyleSheet} from 'react-native';
import {Text, Card} from 'react-native-paper';
import CustomTheme from "../../../assets/Theme"


export default function PersonInformationNFC({style, person}) {
    // Assuming person is passed as a prop with name, surname, and dateOfBirth properties
    return (
        <View style={style}>
            <Card>
                <Card.Title title="Person Information from NFC" titleStyle={{fontSize: 17, fontWeight: 'bold' }} style={{padding: -5}}/>
                <Card.Content>
                    <View style={styles.containerRow}>
                        <View style={styles.containerColumn}>
                            <Image
                                source={{uri: 'https://thumbs.mugshots.com/gallery/images/f0/ef/JONATHAN-XAVIER-CABRAL-mugshot-50338840.400x800.jpg'}}
                                style={styles.image}
                            />
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Surname:</Text>
                                <Text>{person.surname}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Name:</Text>
                                <Text>{person.name}</Text>
                            </View>
                        </View>
                        <View style={styles.containerColumn}>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Date of Birth:</Text>
                                <Text>{person.dateOfBirth.toLocaleDateString()}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Surname 22:</Text>
                                <Text>{person.surname}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Name 2:</Text>
                                <Text>{person.name}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Date of Birth 2:</Text>
                                <Text>{person.dateOfBirth.toLocaleDateString()}</Text>
                            </View>
                        </View>
                    </View>
                </Card.Content>
            </Card>
        </View>
    )
        ;
};
const styles = StyleSheet.create({
    containerRow: {
        //flex: 1,
        flexDirection: 'row', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
        marginBottom: 5,
        alignItems: 'flex-start', // Ensures content aligns at the top
        justifyContent: 'flex-start', // Spreads columns apart if needed
    },
    containerColumn: {
        //flex: 1,
        flexDirection: 'column', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
        marginLeft: 5,
    },
    image: {
        width: 150,
        height: 150,
        resizeMode: 'contain',
    },
});