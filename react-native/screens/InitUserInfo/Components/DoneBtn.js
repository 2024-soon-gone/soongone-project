import React from 'react';
import { Pressable, StyleSheet, Text } from 'react-native';
import theme from '../../../assets/Theme';
function DoneButton({ onPress }) {
  return (
    <Pressable style={styles.button}>
      <Text style={styles.text} onPress={onPress}>
        완료
      </Text>
    </Pressable>
  );
}
const styles = StyleSheet.create({
  button: {
    display: 'flex',
    width: 74,
    height: 32,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 10,
    backgroundColor: theme.black,
  },
  text: {
    color: theme.white,
    fontSize: 12,
  },
});

export default DoneButton;
