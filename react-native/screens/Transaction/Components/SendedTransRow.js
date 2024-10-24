import { View, Text, Image, StyleSheet } from 'react-native';
import typo from '../../../assets/Typograph';
import theme from '../../../assets/Theme';
import { Unix_timestamp } from '../../../Utils/TimeUtils';

const SendedTransRow = ({ data }) => {
  // bidsPlacedByUser array의 원소
  const time = Unix_timestamp(data.bidDTO.endTime).split(' ');

  return (
    <View style={styles.root}>
      <Image source={{ uri: data.imgUrl }} style={styles.image} />
      <View style={styles.center}>
        <Text style={styles.name}>{data.bidderAccountId}</Text>
        <Text style={styles.price}>{data.bidDTO.amountPaymentToken + '$'}</Text>
      </View>
      <View style={styles.tail}>
        <Text style={styles.create}>{time[0]}</Text>
        <Text style={styles.valid}>{time[1]}</Text>
      </View>
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
  },
  image: {
    width: 60,
    height: 60,
    borderRadius: 10,
    marginRight: 12,
  },
  center: {},
  tail: { flex: 1, alignItems: 'flex-end', justifyContent: 'space-evenly' },
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

export default SendedTransRow;
