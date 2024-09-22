// import {
//     View,
// } from "react-native";

import React from "react";


import { Avatar, Button, Card, Text } from 'react-native-paper';
import {View} from "react-native";



export default function Index() {

    const LeftContent = props => <Avatar.Icon {...props} icon="folder" />

    return (
        <>
        <View>
            <Text>
                333333
            </Text>
        </View>
        <Card>
            <Card.Title title="Card Title" subtitle="Card Subtitle" left={LeftContent} />
            <Card.Content>
                <Text variant="titleLarge">Card title</Text>
                <Text variant="bodyMedium">Card content</Text>
            </Card.Content>
            <Card.Cover source={{ uri: 'https://picsum.photos/700' }} />
            <Card.Actions>
                <Button>Cancel</Button>
                <Button>Ok</Button>
            </Card.Actions>
        </Card>
        </>
    );
};
