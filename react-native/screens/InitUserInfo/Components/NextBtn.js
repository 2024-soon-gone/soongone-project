import React from 'react';
import { Pressable, StyleSheet, Text } from 'react-native';
import theme from '../../../assets/Theme';
function NextButton({ isActivate, onPress }) {
  return (
    <Pressable
      style={{ width: '80%' }}
      onPress={onPress}
      disabled={!isActivate}
    >
      <Text
        style={{
          ...styles.button,
          backgroundColor: isActivate ? theme.black : theme.grey6,
        }}
      >
        다음
      </Text>
    </Pressable>
  );
}
const styles = StyleSheet.create({
  button: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 10,
    height: 56,
    color: theme.white,
    fontSize: 16,
  },
});

export default NextButton;
