import React, {memo, useEffect, useState} from 'react';
import { TouchableOpacity, StyleSheet, Text, View } from 'react-native';
import Background from '../components/Background';
import Logo from '../components/Logo';
import Header from '../components/Header';
import Button from '../components/Button';
import TextInput from '../components/TextInput';
import  customTheme  from '../assets/Theme';
import { usernameValidator, passwordValidator } from '../utils/utils';
import { Navigation } from '../utils/types.tsx';
import {useDispatch} from "react-redux";
import {newLoginCredentials, newUser} from "../utils/model.ts";
import {setUser} from "../store/userData";
import {login} from "../utils/axiosCall.ts";


type Props = {
    navigation: Navigation;
};

const LoginScreen = ({ navigation }: Props) => {
    //Necessary state for user input
    const [username, setUsername] = useState('sadfasdf');
    const [password, setPassword] = useState('asdfasf');
    const dispatch = useDispatch();
    const onLoginPressed = () => {
        const usernameError = usernameValidator(username);
        const passwordError = passwordValidator(password);

        //Delete this if it's not needed
        // if (usernameError || passwordError) {
        //     setUsername({ ...username, error: usernameError });
        //     setPassword({ ...password, error: passwordError });
        //     return;
        // }


        //TODO Make an axios call to fetch the currentUser
        login(newLoginCredentials(username, password)).then((res) => {
            const currentUser =res;
            //const currentUser = newUser('Telikos user', '234532', '08:00 - 19:00');
            dispatch(setUser(currentUser));
            //If all is well, navigate to the next Screen
            navigation.navigate('Dashboard');
        });
    };

    //The image our Screen will pass to the Background component
    const imagePassed = require('../assets/Server_room.jpg')

    return (

        <Background imageUrl={imagePassed}>
            {/*<BackButton goBack={() => navigation.navigate('HomeScreen')} />*/}
            <View style={styles.container}>
            <Logo marginBottom={0}/>
            <Header >Entry Exit System</Header>
            <TextInput
                label="Username"
                returnKeyType="next"
                value={username.value}
                onChangeText={text => setUsername({ value: text, error: '' })}
                error={!!username.error}
                errorText={username.error}
                autoCapitalize="none"
                textContentType="username"
                keyboardType="default"
            />

            <TextInput
                label="Password"
                returnKeyType="done"
                value={password.value}
                onChangeText={text => setPassword({ value: text, error: '' })}
                error={!!password.error}
                errorText={password.error}
                secureTextEntry
            />
            <Button mode="contained" onPress={onLoginPressed}>
                Είσοδος
            </Button>
            </View>
        </Background>
    );
};

const styles = StyleSheet.create({
    container: {
        width: '100%',
        alignItems: 'center',
        backgroundColor: 'white',
        padding: 15,
        borderRadius: 15,
    },
    forgotPassword: {
        width: '100%',
        alignItems: 'flex-end',
        marginBottom: 24,
    },
    row: {
        flexDirection: 'row',
        marginTop: 4,
    },
    label: {
        color: customTheme.colors.secondary,
    },
    link: {
        fontWeight: 'bold',
        color: customTheme.colors.primary,
    },
});

export default LoginScreen;
