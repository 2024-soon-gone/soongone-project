import { View, Text, Image, StyleSheet, Pressable } from 'react-native';
import typo from '../../../assets/Typograph';
import theme from '../../../assets/Theme';
import AcceptButton from './AcceptButton';

const ReceivedTransRow = ({ data }) => {
  return (
    <View style={styles.root}>
      <Image source={{ uri: data.image }} style={styles.image} />
      <View style={styles.center}>
        <Text style={styles.name}>{data.name}</Text>
        <Text style={styles.price}>{data.price + '$'}</Text>
      </View>
      <View style={styles.tail}>
        <Text style={styles.valid}>{data.validDate}</Text>
      </View>
      <AcceptButton />
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    width: '100%',
    height: 72,
    flexDirection: 'row',
    paddingHorizontal: 20,
    paddingVertical: 8,
    alignItems: 'center',
  },
  image: {
    width: 60,
    height: 60,
    borderRadius: 10,
    marginRight: 12,
  },
  center: { height: '100%' },
  tail: {
    flex: 1,
    alignItems: 'flex-end',
    justifyContent: 'space-evenly',
    marginRight: 16,
  },
  name: {
    ...typo.body,
    flex: 1,
    color: theme.grey6,
  },
  price: {
    ...typo.H4,
    flex: 1,
  },
  create: {
    ...typo.label_medium,
    color: theme.grey4,
  },
  valid: {
    ...typo.bold,
    color: theme.grey6,
  },
});

export default ReceivedTransRow;
