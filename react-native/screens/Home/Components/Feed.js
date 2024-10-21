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
import Deal from '../../../assets/icon/deal';

const WINDOW_WIDTH = Dimensions.get('window').width;

const Feed = ({
  postId,
  accountId,
  imageUrl,
  likes,
  comments,
  text,
  onBidPress,
}) => {
  return (
    <View style={styles.root}>
      <View style={styles.header}>
        <Image
          style={styles.profileImg}
          source={require('../../../assets/profileDefault.png')}
        />
        <Text style={typo.body}>{accountId}</Text>
      </View>
      <Image style={styles.mainImg} source={{ uri: imageUrl }}></Image>
      <View style={styles.likeComment}>
        <Heart style={{ marginTop: 4 }}></Heart>
        {!likes && <Text style={styles.numbers}>{likes}</Text>}
        <Bubble style={{ marginLeft: 12, marginTop: 4 }}></Bubble>
        {!comments && <Text style={styles.numbers}>{comments}</Text>}
        <Pressable
          style={({ pressed }) => {
            return { opacity: pressed ? 0.5 : 1, ...styles.bid };
          }}
          onPress={() => onBidPress(postId)}
          hitSlop={{ bottom: 50, left: 50, right: 50, top: 50 }}
        >
          <Deal stroke={'black'} strokeWidth="2" strokeLinejoin="round" />
        </Pressable>
      </View>
      <View style={{ display: 'inline' }}>
        <Text style={typo.bold}>{'soongone_'}</Text>
        <Text style={typo.body}>{text}</Text>
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
    borderRadius: 16,
    overflow: 'hidden',
    backgroundColor: 'black',
    resizeMode: 'contain',
    marginEnd: 8,
  },
  mainImg: {
    height: WINDOW_WIDTH * 0.9,
    width: WINDOW_WIDTH * 0.9,
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
  bid: {
    marginTop: 4,
    position: 'absolute',
    right: 4,
  },
});

export default Feed;
