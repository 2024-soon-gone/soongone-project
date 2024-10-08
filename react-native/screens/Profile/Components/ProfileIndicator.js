import { View, Text, Pressable, Image, StyleSheet } from 'react-native';
import typo from '../../../assets/Typograph';
const ProfileIndicator = ({ value, label }) => {
  return (
    <View style={styles.root}>
      <Text style={{ ...typo.H4, flex: '1' }}>{value}</Text>
      <Text style={{ ...typo.label }}>{label}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    width: '85px',
    height: '52px',
    alignItems: 'center',
  },
});

export default ProfileIndicator;
