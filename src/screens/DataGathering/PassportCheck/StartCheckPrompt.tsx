import React from "react";
import {Button, Card, Text} from "react-native-paper";
import {NativeModules} from 'react-native';
import {Navigation} from "../../../utils/types.tsx";


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
        <>
            <Card >
                <Card.Title title="Card Title" subtitle="Card Subtitle"  />
                {/*<Card.Content>*/}

                {/*    <Text variant="titleLarge">Card title</Text>*/}
                {/*    <Text variant="bodyMedium">Card content</Text>*/}
                {/*</Card.Content>*/}
                {/*<Card.Cover source={{ uri: 'https://picsum.photos/700' }} />*/}
                <Card.Actions>
                    <Button>Cancel</Button>
                    <Button onPress={onPress}>Ok</Button>
                </Card.Actions>
            </Card>
        </>
    );
};
