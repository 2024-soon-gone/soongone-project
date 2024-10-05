import {
  View,
  TextInput,
  StyleSheet,
  Dimensions,
  Image,
  ScrollView,
} from 'react-native';
import SearchIcon from '../../assets/icon/search';
import theme from '../../assets/Theme';

const WINDOW_WIDTH = Dimensions.get('window').width;
const IMG_WIDTH = (WINDOW_WIDTH - 8) / 3;

const Search = () => {
  return (
    <View style={styles.root}>
      <View style={styles.searchBar}>
        <SearchIcon width={20} stroke={theme.grey3} />
        <TextInput
          style={styles.textinput}
          placeholder="검색..."
          placeholderTextColor={theme.grey3}
        ></TextInput>
      </View>
      <ScrollView style={{ flex: 1 }}>
        <View style={styles.result}>
          <Image style={styles.img} backgroundColor={theme.red2}></Image>
          <Image style={styles.img} backgroundColor={theme.red2}></Image>
          <Image style={styles.img} backgroundColor={theme.red2}></Image>
          <Image style={styles.img} backgroundColor={theme.red2}></Image>
          <Image style={styles.img} backgroundColor={theme.red2}></Image>
          <Image style={styles.img} backgroundColor={theme.red2}></Image>
          <Image style={styles.img} backgroundColor={theme.red2}></Image>
          <Image style={styles.img} backgroundColor={theme.red2}></Image>
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
  },
  searchBar: {
    flexDirection: 'row',
    height: '57px',
    borderColor: theme.grey1,
    borderWidth: '1px',
    borderRadius: '6px',
    width: '90%',
    height: '36px',
    backgroundColor: theme.white,
    alignItems: 'center',
    paddingHorizontal: '12px',
    paddingVertical: '8px',
    gap: '8px',
    marginBottom: '20px',
  },
  textinput: {
    height: '14px',
    flex: '1',
  },
  result: {
    width: '100%',
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'flex-start',
    gap: '2px',
    padding: '2px',
  },
  img: {
    width: IMG_WIDTH,
    height: IMG_WIDTH,
    backgroundColor: theme.red3,
  },
});

export default Search;
