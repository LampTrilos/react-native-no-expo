// import {
//     View,
// } from "react-native";

import React from "react";
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
import {NavigationContainer} from "@react-navigation/native";
import FakeScreen from "./FakeScreen.tsx";
import FakeScreen2 from "./FakeScreen2.tsx";
import Logo from '../components/Logo';

type Props = {
    navigation: Navigation;
};

export default function Dashboard({navigation}: Props) {
    const Drawer = createDrawerNavigator();

    // const { LightTheme } = adaptNavigationTheme({ reactNavigationLight: DefaultTheme });
    return (

        <Drawer.Navigator screenOptions={{drawerType: 'slide'}} initialRouteName="FakeScreen"
                          drawerContent={(props) => <CustomDrawerContent {...props} />}>
            <Drawer.Screen name="FakeScreen" component={FakeScreen} options={({navigation}) => ({
                headerShown: true,
                headerLeft: () => (<Button icon="account"
                                           onPress={() => navigation.toggleDrawer()}/>),
            })}
            />
            <Drawer.Screen name="FakeScreen2" component={FakeScreen2} options={{headerShown: false}}/>
            {/*<Drawer.Screen name="Dashboard" component={Dashboard} options={{ headerShown: false }}/>*/}
        </Drawer.Navigator>)


    // First Stack of the app is composed of the Login screen and the Dashboard of the app*
    {/*<NavigationContainer independent={true}>*/
    }
    {/*</NavigationContainer>*/
    }
    {/*<Card>*/
    }
    {/*    <Card.Title title="Card Title" subtitle="Card Subtitle"  />*/
    }
    {/*    <Card.Content>*/
    }
    {/*        <Text variant="titleLarge">Card title</Text>*/
    }
    {/*        <Text variant="bodyMedium">Card content</Text>*/
    }
    {/*    </Card.Content>*/
    }
    {/*    <Card.Cover source={{ uri: 'https://picsum.photos/700' }} />*/
    }
    {/*    <Card.Actions>*/
    }
    {/*        <Button>Cancel</Button>*/
    }
    {/*        <Button>Ok</Button>*/
    }
    {/*    </Card.Actions>*/
    }
    {/*</Card>*/
    }
    //);
};

// Custom Drawer Content
function CustomDrawerContent(props) {
    const [active, setActive] = React.useState('first');
    return (
        <>
            <PaperDrawer.Section>
                <Logo/>
            </PaperDrawer.Section>
            <PaperDrawer.Section>
                <PaperDrawer.Item
                    label="First Item"
                    active={active === 'first'}
                    onPress={() => {
                        setActive('first');
                        props.navigation.navigate('FakeScreen');
                    }}
                />
                <PaperDrawer.Item
                    label="Second Item"
                    active={active === 'second'}
                    onPress={() => {
                        setActive('second');
                        props.navigation.navigate('FakeScreen2');
                    }}
                />
            </PaperDrawer.Section>
        </>
    );
}