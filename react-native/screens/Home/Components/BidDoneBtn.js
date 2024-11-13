import React from 'react';
import { Pressable, StyleSheet, Text } from 'react-native';
import theme from '../../../assets/Theme';
function BidDoneButton({ onPress }) {
  return (
    <Pressable
      style={{
        width: '80%',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 10,
        height: 56,
        backgroundColor: theme.black,
      }}
      onPress={onPress}
    >
      <Text
        style={{
          ...styles.button,
        }}
      >
        제안하기
      </Text>
    </Pressable>
  );
}
const styles = StyleSheet.create({
  button: {
    color: theme.white,
    fontSize: 16,
  },
});

export default BidDoneButton;
