import React, { memo } from 'react';
import { Image, StyleSheet } from 'react-native';

const Logo = (props) => (
  <Image source={require('../assets/Greek_police_logo.png')} style={[styles.image, { marginBottom: props.marginBottom }]} />
);

const styles = StyleSheet.create({
  image: {
    width: 111,
    height: 75,
    paddingTop: 120
  },
});

export default memo(Logo);
