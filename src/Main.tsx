import * as React from 'react';
import {AppRegistry} from 'react-native';
import {DefaultTheme, Provider as PaperProvider} from 'react-native-paper';
import {Provider as StoreProvider} from 'react-redux';
import {name as appName} from '../app.json';
import App from './App';
import store from './store/store';
import customTheme from './assets/Theme'
import 'react-native-gesture-handler';

//This is the entry point of the App

export default function Main() {
    return (
        // <StoreProvider store={store}>
            <PaperProvider theme={customTheme}>
                <App/>
            </PaperProvider>
        // </StoreProvider>
    );
}

AppRegistry.registerComponent(appName, () => Main);