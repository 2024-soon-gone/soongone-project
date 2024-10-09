import {
  View,
  Text,
  Image,
  StyleSheet,
  Dimensions,
  Pressable,
} from 'react-native';
import theme from '../../../assets/Theme';
import typo from '../../../assets/Typograph';
import Bubble from '../../../assets/icon/bubble';
import Heart from '../../../assets/icon/heart';

const WINDOW_WIDTH = Dimensions.get('window').width;

const Feed = ({ profileUrl, userId, imageUrl, likes, comments }) => {
  return (
    <View style={styles.root}>
      <View style={styles.header}>
        <Image style={styles.profileImg} />
        <Text style={typo.body}>{'userId'}</Text>
      </View>
      <Image style={styles.mainImg}></Image>
      <View style={styles.likeComment}>
        <Heart style={{ marginTop: 4 }}></Heart>
        {!likes && <Text style={styles.numbers}>{70}</Text>}
        <Bubble style={{ marginLeft: 12, marginTop: 4 }}></Bubble>
        {!comments && <Text style={styles.numbers}>{153}</Text>}
      </View>
      <View style={{ display: 'inline' }}>
        <Text style={typo.bold}>{'soongone_'}</Text>
        <Text style={typo.body}>
          {
            '순간 @soongone_ 과 함께 순간과 함께순간과 함께 순간과 함께 순간과 함께 순간과 함께 순간...'
          }
        </Text>
      </View>
      <Pressable>
        <Text style={{ ...typo.label_light, color: theme.grey4 }}>
          댓글 모두 보기
        </Text>
      </Pressable>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    width: '100%',
    paddingHorizontal: 20,
    marginVertical: 10,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  profileImg: {
    width: 32,
    height: 32,
    borderRadius: 10,
    overflow: 'hidden',
    backgroundColor: theme.blue3,
    marginEnd: 8,
  },
  mainImg: {
    height: WINDOW_WIDTH * 0.9,
    width: WINDOW_WIDTH * 0.9,
    backgroundColor: theme.black,
    marginBottom: 8,
  },
  likeComment: {
    height: 28,
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  numbers: {
    ...typo.body,
  },
});

export default Feed;
