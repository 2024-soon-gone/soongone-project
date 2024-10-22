import { View, Text, Image, StyleSheet, Pressable } from 'react-native';
import typo from '../../../assets/Typograph';
import theme from '../../../assets/Theme';
import AcceptButton from './AcceptButton';
import { Unix_timestamp } from '../../../Utils/TimeUtils';

const ReceivedTransRow = ({ data }) => {
  var time = Unix_timestamp(data.bidDTO.endTime).split(' ');
  time = time[0] + '\n' + time[1];
  return (
    <View style={styles.root}>
      <Image source={{ uri: data.imgUrl }} style={styles.image} />
      <View style={styles.center}>
        <Text style={styles.name}>{data.bidderAccountId}</Text>
        <Text style={styles.price}>{data.bidDTO.amountPaymentToken + '$'}</Text>
      </View>
      <View style={styles.tail}>
        <Text style={styles.valid}>{time}</Text>
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
    textAlign: 'right',
  },
});

export default ReceivedTransRow;
