import React from 'react';
import { View, Button, StyleSheet } from 'react-native';

const LeftRightButtonsLayout = ({ children }) => {
    return (
        <View style={styles.container}>
            <View style={styles.sideButton}>
                <Button title="---------ΠΡΟΗΓΟΥΜΕΝΟ-----------------------------" onPress={() => {}} />
            </View>
            <View style={styles.content}>
                {children}
            </View>
            <View style={styles.sideButton}>
                <Button title="---------------ΕΠΟΜΕΝΟ-----------------------------" onPress={() => {}} />
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        paddingHorizontal: 0,
        height: '100%',
    },
    sideButton: {
        padding: 35,
        width: 100, // Adjust the width as needed
    },
    content: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
});

export default LeftRightButtonsLayout;