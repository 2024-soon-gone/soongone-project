import { View, StyleSheet, Text } from 'react-native';
import SendedTransRow from './SendedTransRow';
import typo from '../../../assets/Typograph';

const SendedTransSeg = ({ data }) => {
  var isData = data.length > 0;
  return (
    <View style={styles.root}>
      {isData ? (
        data.map((trans, i) => (
          <SendedTransRow data={trans} key={trans.bidDTO.nftId} />
        ))
      ) : (
        <Text style={styles.empty}>거래를 시작해 보세요</Text>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
  },
  empty: {
    ...typo.H4,
  },
});

export default SendedTransSeg;
