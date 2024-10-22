import { View, StyleSheet, Text, ScrollView, Image } from 'react-native';
import ReceivedTransRow from './ReceivedTransRow';
import typo from '../../../assets/Typograph';
const ReceivedTransSeg = ({ data }) => {
  const isData = data.length > 0;
  return (
    <View style={styles.root}>
      <View style={{ width: '100%', alignItems: 'center' }}>
        {isData ? (
          data.map((trans, i) => (
            <ReceivedTransRow data={trans} key={trans.bidDTO.nftId} />
          ))
        ) : (
          <Text style={styles.empty}>아직 제안 온 거래가 없어요.</Text>
        )}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
  },
  header: {
    height: 72,
    width: '100%',
  },
  empty: {
    ...typo.H4,
  },
});

export default ReceivedTransSeg;
