import React from 'react';
import { Pressable, StyleSheet, Text } from 'react-native';
import theme from '../../../assets/Theme';
function DoneButton({ onPress }) {
  return (
    <Pressable>
      <Text style={styles.button} onPress={onPress}>
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
    color: theme.white,
    fontSize: 12,
    backgroundColor: theme.black,
  },
});

export default DoneButton;
