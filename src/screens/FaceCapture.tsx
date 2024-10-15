
import React from "react";
import {Button, Card, Text} from "react-native-paper";
import {NativeModules} from 'react-native';



export default function FaceCapture() {
    const {CalendarModule} = NativeModules;
    const {BiometricModule} = NativeModules;
    CalendarModule.createCalendarEvent('testName', 'testLocation');
    console.log('Calling Biometric Module.........................ASDFASDFSADFASDFSAFD')
    BiometricModule.navigateToMultiModalActivity();
    const onPress = () => {
        //CalendarModule.createCalendarEvent('testName', 'testLocation');

    };
    return (
        <>
            <Card>
                <Card.Title title="Card Title" subtitle="Card Subtitle"  />
                <Card.Content>

                    <Text variant="titleLarge">Card title</Text>
                    <Text variant="bodyMedium">Card content</Text>
                </Card.Content>
                <Card.Cover source={{ uri: 'https://picsum.photos/700' }} />
                <Card.Actions>
                    <Button>Cancel</Button>
                    <Button onPress={onPress}>Ok</Button>
                </Card.Actions>
            </Card>
        </>
    );
};
