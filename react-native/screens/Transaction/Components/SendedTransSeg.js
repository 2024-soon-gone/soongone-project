import { View, StyleSheet, Text } from 'react-native';
import SendedTransRow from './SendedTransRow';

const SendedTransSeg = ({ data }) => {
  return (
    <View style={styles.root}>
      {data.map((trans, i) => (
        <SendedTransRow data={trans} key={i} />
      ))}
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
  },
});

export default SendedTransSeg;
