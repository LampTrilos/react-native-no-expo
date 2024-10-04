import React, {useEffect, useState} from 'react';
import {View, ScrollView, Image, StyleSheet} from 'react-native';
import {Text, Card, IconButton} from 'react-native-paper';
import CustomTheme from "../../../assets/Theme"
import CheckIcon from "../../../components/CheckIcon.tsx";


export default function PersonInformationNFC({style, person}) {
    // Assuming person is passed as a prop with name, surname, and dateOfBirth properties

    //------------------------------------Section to check if the passport valid until date is past today-----------------------------//
    // Function to check if passportExpiry is past today
    const [isPassportExpired, setIsPassportExpired] = useState(false);
    const checkDate = () => {
        // Get today's date at midnight
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        // Create a date object from person.date2
        const passportExpiry = new Date(person.validUntil);
        // Check if date2 is before today
        setIsPassportExpired(passportExpiry < today);
    }
    // useEffect to run the check when the component mounts or when person.date2 changes
    useEffect(() => {
        checkDate();
    }, [person.validUntil]); // Dependency array: check whenever person.validUntil changes
    //------------------------------------End of section to check if the passport valid until date is past today-----------------------------//

    return (
        <View style={style}>
            <Card style={{height: '100%'}}>
                <Card.Title title="Person Information from NFC" titleStyle={{fontSize: 17, fontWeight: 'bold'}}
                            style={{padding: -5}}/>
                <Card.Content >
                    <View style={styles.containerRow}>
                        <View style={styles.containerColumn}>
                            <Image
                                source={{uri: 'https://thumbs.mugshots.com/gallery/images/f0/ef/JONATHAN-XAVIER-CABRAL-mugshot-50338840.400x800.jpg'}}
                                style={styles.image}
                            />
                            <CheckIcon textShown={"Chip"} attributeChecked={person.chipChecked}/>
                            <CheckIcon textShown={"MRZ"} attributeChecked={person.mrzChecked}/>
                        </View>
                        <View style={styles.containerColumn}>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Family Name:</Text>
                                <Text>{person.familyName}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>First Name:</Text>
                                <Text>{person.firstName}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Nationality:</Text>
                                <Text>{person.nationality}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Date of Birth:</Text>
                                <Text>{person.dateOfBirth.toLocaleDateString()}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Gender:</Text>
                                <Text>{person.gender}</Text>
                            </View>
                        </View>
                        <View style={styles.containerColumn}>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Issue Country:</Text>
                                <Text>{person.issueCountry}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Document Number:</Text>
                                <Text>{person.documentNumber}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Valid until:</Text>
                                <Text>{person.validUntil.toLocaleDateString()}</Text>
                            </View>
                        </View>
                    </View>
                    <View>
                        {/* Show red exclamation mark and red text if passportExpiry is past */}
                        {isPassportExpired && (
                            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                                <IconButton icon="alert-circle" iconColor="red" size={24} />
                                <Text style={{ color: 'red', fontSize: 14 }}>Document is invalid due to expiration date</Text>
                            </View>
                        )}
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
        paddingVertical: 15
    },
    containerColumn: {
        //flex: 1,
        flexDirection: 'column', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
        marginLeft: 5,
        paddingHorizontal: 10
    },
    image: {
        width: 150,
        height: 150,
        resizeMode: 'contain',
    },
});
