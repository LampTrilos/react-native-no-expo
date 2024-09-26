import React from 'react';
import { View, ScrollView, Image, StyleSheet } from 'react-native';
import { Text, Card } from 'react-native-paper';

export default function PersonInformation({style, person})  {
    // Assuming person is passed as a prop with name, surname, and dateOfBirth properties
    return (
        <View style={style}>
            <Card>
                <Card.Title title="Person Information"  titleStyle={{fontSize: 17,  fontWeight: 'bold'}}/>
                <Card.Content>
                    <Image
                        source={{ uri: 'https://thumbs.mugshots.com/gallery/images/f0/ef/JONATHAN-XAVIER-CABRAL-mugshot-50338840.400x800.jpg' }} // Replace with your image URL
                        style={styles.image}
                    />
                    <View style={{ marginBottom: 16 }}>
                        <Text style={{ fontWeight: 'bold' }}>Surname:</Text>
                        <Text>{person.surname}</Text>
                    </View>
                    <View style={{ marginBottom: 16 }}>
                        <Text style={{ fontWeight: 'bold' }}>Name:</Text>
                        <Text>{person.name}</Text>
                    </View>
                    <View style={{ marginBottom: 16 }}>
                        <Text style={{ fontWeight: 'bold' }}>Date of Birth:</Text>
                        <Text>{person.dateOfBirth.toLocaleDateString()}</Text>
                    </View>
                    <View style={{ marginBottom: 16 }}>
                        <Text style={{ fontWeight: 'bold' }}>Surname:</Text>
                        <Text>{person.surname}</Text>
                    </View>
                    <View style={{ marginBottom: 16 }}>
                        <Text style={{ fontWeight: 'bold' }}>Name:</Text>
                        <Text>{person.name}</Text>
                    </View>
                    <View style={{ marginBottom: 16 }}>
                        <Text style={{ fontWeight: 'bold' }}>Date of Birth:</Text>
                        <Text>{person.dateOfBirth.toLocaleDateString()}</Text>
                    </View>
                </Card.Content>
            </Card>
        </View>
    );
};
const styles = StyleSheet.create({
    card: {
        margin: 16,
    },
    image: {
        width: 150,
        height: 150,
        resizeMode: 'cover',
    },
});