import React from 'react';
import { Pressable, StyleSheet, Text } from 'react-native';
import theme from '../../../assets/Theme';
function AcceptButton({ onPress }) {
  return (
    <Pressable>
      <Text style={styles.button} onPress={onPress}>
        수락
      </Text>
    </Pressable>
  );
}
const styles = StyleSheet.create({
  button: {
    display: 'flex',
    width: 53,
    height: 32,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 8,
    color: theme.white,
    fontSize: 12,
    backgroundColor: theme.black,
  },
});

export default AcceptButton;
