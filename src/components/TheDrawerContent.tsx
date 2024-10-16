import {StyleSheet, Text, View} from 'react-native';
import React from "react";
import Logo from "./Logo.tsx";
import customTheme from "../assets/Theme";
import {
    Drawer as PaperDrawer,
} from 'react-native-paper';


// Custom Drawer Content
export default function CustomDrawerContent(props) {
    const [active, setActive] = React.useState('first');
    return (
        <>
            {/*<PaperDrawer.Section>*/}
            <View style={{alignItems: 'center', paddingTop: 22}}>
                <Logo marginBottom={20}/>
                <Text style={{color: 'white', fontWeight: 'bold', fontSize: 22}}>
                    Enty Exit System
                </Text>
            </View>
            {/*</PaperDrawer.Section>*/}
            <PaperDrawer.Section style={{paddingTop: 22}}>
                <PaperDrawer.Item
                    label="Dashboard"
                    icon={"view-dashboard"}
                    theme={drawerItemTheme}
                    active={active === 'first'}
                    onPress={() => {
                        setActive('first');
                        props.navigation.navigate('PassportCheck');
                    }}
                />
                <PaperDrawer.Item
                    label="Border Control - Entry"
                    icon={"passport"}
                    theme={drawerItemTheme}
                    active={active === 'second'}
                    onPress={() => {
                        setActive('second');
                        props.navigation.navigate('FakeScreen2');
                    }}
                />
                <PaperDrawer.Item
                    label="Border Control - Exit"
                    icon={"passport"}
                    theme={drawerItemTheme}
                    active={active === 'third'}
                    onPress={() => {
                        setActive('third');
                        props.navigation.navigate('FakeScreen2');
                    }}
                />
            </PaperDrawer.Section>
        </>
    );
}

const styles = StyleSheet.create({
    drawer: {
        backgroundColor: customTheme.colors.drawerContainer,
    },
});

//We override the theme on the inactive Drawer item text
const drawerItemTheme = {
    fonts: {labelLarge: {fontWeight: '550', fontSize: 19}},
    colors: {
        //We override the color on the inactive Drawer item text
        "onSurfaceVariant": "rgb(236,242,244)",
    }
}