// import {
//     View,
// } from "react-native";

import React, {useEffect, useRef} from "react";
import {
    adaptNavigationTheme,
    Avatar,
    Button,
    Card,
    DefaultTheme,
    Drawer as PaperDrawer,
    Icon,
    Text
} from 'react-native-paper';
import {Navigation} from '../utils/types.tsx';
import {createDrawerNavigator, DrawerContent, DrawerContentScrollView, DrawerItem} from "@react-navigation/drawer";
import {DrawerActions} from '@react-navigation/native';
import {NavigationContainer} from "@react-navigation/native";
import FakeScreen from "./FakeScreen.tsx";
import FakeScreen2 from "./FakeScreen2.tsx";
import Logo from '../components/Logo';
import customTheme from '../assets/Theme'
import {StyleSheet, View} from "react-native"
import PassportCheck from "./PassportCheck/PassportCheck.tsx";

type Props = {
    navigation: Navigation;
};

export default function Dashboard({navigation}: Props) {
    const Drawer = createDrawerNavigator();

    // On load of the page, we open the Drawer programmatically
    const drawerRef = useRef(null);
    useEffect(() => {
        navigation.dispatch(DrawerActions.openDrawer());
    }, []);


    return (
        <Drawer.Navigator drawerRef={drawerRef} screenOptions={{
            drawerType: 'slide',
            drawerStyle: styles.drawer
        }}
                          initialRouteName="FakeScreen"
                          drawerContent={(props) =>
                              <CustomDrawerContent {...props} />}>
            <Drawer.Screen name="PassportCheck" component={PassportCheck} options={({navigation}) => ({
                headerShown: true,
                headerLeft: () => (<Button icon="menu"
                                           onPress={() => navigation.toggleDrawer()}/>),
            })}
            />
            <Drawer.Screen name="FakeScreen2" component={FakeScreen} options={{headerShown: false}}/>
            {/*<Drawer.Screen name="Dashboard" component={Dashboard} options={{ headerShown: false }}/>*/}
        </Drawer.Navigator>)
};

// Custom Drawer Content
function CustomDrawerContent(props) {
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
                    theme={{fonts: {labelLarge: {fontWeight: '550', fontSize: 19}}}}
                    active={active === 'first'}
                    onPress={() => {
                        setActive('first');
                        props.navigation.navigate('FakeScreen');
                    }}
                />
                <PaperDrawer.Item
                    label="Border Control - Entry"
                    icon={"passport"}
                    theme={{fonts: {labelLarge: {fontWeight: '550', fontSize: 19}}}}
                    active={active === 'second'}
                    onPress={() => {
                        setActive('second');
                        props.navigation.navigate('FakeScreen2');
                    }}
                />
                <PaperDrawer.Item
                    label="Border Control - Exit"
                    icon={"passport"}
                    theme={{fonts: {labelLarge: {fontWeight: '550', fontSize: 19}}}}
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