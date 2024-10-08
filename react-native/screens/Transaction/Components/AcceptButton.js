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
    width: '53px',
    height: '32px',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: '8px',
    color: theme.white,
    fontSize: '12px',
    backgroundColor: theme.black,
  },
});

export default AcceptButton;