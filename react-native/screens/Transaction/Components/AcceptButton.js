import React from 'react';
import { Pressable, StyleSheet, Text } from 'react-native';
import theme from '../../../assets/Theme';
function AcceptButton({ onPress }) {
  return (
    <Pressable
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        width: 53,
        height: 32,
        borderRadius: 8,
        backgroundColor: theme.black,
      }}
    >
      <Text style={styles.button} onPress={onPress}>
        수락
      </Text>
    </Pressable>
  );
}
const styles = StyleSheet.create({
  button: {
    color: theme.white,
    fontSize: 12,
  },
});

export default AcceptButton;
