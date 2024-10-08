import { View, Text, Image, StyleSheet } from 'react-native';
import typo from '../../../assets/Typograph';
import theme from '../../../assets/Theme';

const SendedTransRow = ({ data }) => {
  return (
    <View style={styles.root}>
      <Image source={{ uri: data.image }} style={styles.image} />
      <View style={styles.center}>
        <Text style={styles.name}>{data.name}</Text>
        <Text style={styles.price}>{data.price + '$'}</Text>
      </View>
      <View style={styles.tail}>
        <Text style={styles.create}>{data.createTime}</Text>
        <Text style={styles.valid}>{data.validDate}</Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    width: '100%',
    height: '72px',
    flexDirection: 'row',
    paddingHorizontal: '20px',
    paddingVertical: '8px',
  },
  image: {
    width: '60px',
    height: '60px',
    borderRadius: '10px',
    marginRight: '12px',
  },
  center: {},
  tail: { flex: '1', alignItems: 'flex-end', justifyContent: 'space-evenly' },
  name: {
    ...typo.body,
    flex: '1',
    color: theme.grey6,
  },
  price: {
    ...typo.H4,
    flex: '1',
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

export default SendedTransRow;
