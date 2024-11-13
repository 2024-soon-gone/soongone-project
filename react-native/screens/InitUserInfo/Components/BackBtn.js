import React from 'react';
import { Pressable } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';
function BackButton({ onPress }) {
  return (
    <Pressable onPress={onPress}>
      <Icon name="chevron-left" size={24} />
    </Pressable>
  );
}

export default BackButton;
