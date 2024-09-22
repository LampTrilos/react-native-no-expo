import React, { memo, useState } from 'react';
import { TouchableOpacity, StyleSheet, Text, View } from 'react-native';
import Background from '../components/Background';
import Logo from '../components/Logo';
import Header from '../components/Header';
import Button from '../components/Button';
import TextInput from '../components/TextInput';
import BackButton from '../components/BackButton';
//import { theme } from '../core/theme';
import  customTheme  from '../assets/Theme';
import { usernameValidator, passwordValidator } from '../core/utils';
import { Navigation } from '../types';

type Props = {
    navigation: Navigation;
};

const LoginScreen = ({ navigation }: Props) => {

    const [username, setUsername] = useState({ value: '', error: '' });
    const [password, setPassword] = useState({ value: '', error: '' });

    const onLoginPressed = () => {
        const usernameError = usernameValidator(username.value);
        const passwordError = passwordValidator(password.value);

        if (usernameError || passwordError) {
            setUsername({ ...username, error: usernameError });
            setPassword({ ...password, error: passwordError });
            return;
        }

        navigation.navigate('Dashboard');
    };

    return (
        <Background>
            {/*<BackButton goBack={() => navigation.navigate('HomeScreen')} />*/}
            <View style={styles.container}>
            <Logo/>
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

            {/*<View style={styles.forgotPassword}>*/}
            {/*    <TouchableOpacity*/}
            {/*        onPress={() => navigation.navigate('ForgotPasswordScreen')}*/}
            {/*    >*/}
            {/*        <Text style={styles.label}>Forgot your password?</Text>*/}
            {/*    </TouchableOpacity>*/}
            {/*</View>*/}

            <Button mode="contained" onPress={onLoginPressed}>
                Είσοδος
            </Button>

            {/*<View style={styles.row}>*/}
            {/*    <Text style={styles.label}>Don’t have an account? </Text>*/}
            {/*    <TouchableOpacity onPress={() => navigation.navigate('RegisterScreen')}>*/}
            {/*        <Text style={styles.link}>Sign up</Text>*/}
            {/*    </TouchableOpacity>*/}
            {/*</View>*/}
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

export default memo(LoginScreen);